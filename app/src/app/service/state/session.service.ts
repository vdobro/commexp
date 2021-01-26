/*
 * Copyright (C) 2020 Vitalijus Dobrovolskis
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
import {BehaviorSubject} from "rxjs";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class SessionService {

	private readonly _session = new BehaviorSubject<Session>({});

	readonly session$ = this._session.asObservable();

	get currentSession(): Session {
		return this._session.getValue();
	}

	constructor() {
	}

	reset() {
		this.setSession({});
	}

	setUser(user: UserSession) {
		this.setSession(user);
	}

	private setSession(val: Session) {
		this._session.next(val);
	}
}


export interface UserSession {
	id: string,
	username: string,
	name: string,
}

export interface AnonymousSession {
}

type Session = UserSession | AnonymousSession
