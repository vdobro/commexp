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

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class SessionService {

	private readonly _session = new BehaviorSubject<Session>({});

	readonly session$: Observable<Session> = this._session.asObservable();

	get current() : Session {
		return this._session.getValue();
	}

	reset() {
		this._session.next({});
	}

	setUser(user: UserSession) {
		this._session.next( user);
	}
}
