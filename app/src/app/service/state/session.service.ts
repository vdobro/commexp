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

import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {Session, UserSession} from "@app/model/user-session";
import {RequestStatus} from "@app/model/request-status";
import {map} from "rxjs/operators";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class SessionService {

	private readonly _session = new BehaviorSubject<SessionState>({
		session: {},
		status: RequestStatus.IDLE
	});

	readonly state$: Observable<SessionState> = this._session.asObservable();
	readonly session$: Observable<Session> = this.state$.pipe(map(x => x.session));

	reset() {
		this._session.next({session: {}, status: RequestStatus.IDLE});
	}

	load() {
		this._session.next({session: {}, status: RequestStatus.LOADING})
	}

	setUser(user: UserSession) {
		this._session.next({session: user, status: RequestStatus.IDLE});
	}
}

export interface SessionState {
	status: RequestStatus,
	session: Session
}
