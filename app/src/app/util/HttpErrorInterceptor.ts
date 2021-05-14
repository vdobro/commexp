/*
 * Copyright (C) 2021 Vitalijus Dobrovolskis
 *
 * This file is part of commexp.
 *
 * commexp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 *
 * commexp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with commexp; see the file LICENSE. If not,
 * see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
import {Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {CredentialsError} from "@app/util/SessionUtils";
import {catchError} from "rxjs/operators";
import {SessionService} from "@app/service/state/session.service";
import {NetworkError} from "@app/util/NetworkError";
import {InternalError} from "@app/util/InternalError";

@Injectable({providedIn: 'root'})
export class HttpErrorInterceptor implements HttpInterceptor {

	constructor(private readonly sessionService: SessionService) {
	}

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		const reqCopy = req.clone({
			withCredentials: true,
			setHeaders: {
				"X-Requested-With": "XMLHttpRequest"
			}
		});
		return next.handle(reqCopy).pipe(catchError(err => this.handleError(err)));
	}

	private handleError(err: HttpErrorResponse): Observable<any> {
		if (err.status === 401 || err.status === 403) {
			this.sessionService.reset();
			return throwError(new CredentialsError());
		} else if (err.status == 500) {
			return throwError(new InternalError());
		}
		return throwError(new NetworkError());
	}
}
