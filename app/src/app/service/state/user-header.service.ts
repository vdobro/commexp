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
import {HeaderService} from "@app/service/state/header.service";
import {AnonymousSession, SessionService, UserSession} from "@app/service/state/session.service";
import {MenuItem} from "primeng/api";
import {PATH_LOGIN, PATH_REGISTER, ROOT_USER} from "@app/util/UrlConfig";
import {isUser} from "@app/util/SessionUtils";
import {AuthService} from "@app/service/auth.service";

@Injectable({
	providedIn: 'root'
})
export class UserHeaderService {

	private readonly loginButton: MenuItem = {
		label: 'Einloggen',
		routerLink: LOGIN,
	};
	private readonly registerButton: MenuItem = {
		label: 'Anmelden',
		routerLink: REGISTER,
	};
	private readonly logoutButton: MenuItem = {
		label: 'Ausloggen',
		command: async event => {
			await this.authService.logout();
		}
	};

	constructor(private readonly header: HeaderService,
	            private readonly sessionService: SessionService,
	            private readonly authService: AuthService) {
		sessionService.session$.subscribe(user => {
			this.updateButtons(user);
		});
	}

	update() {
		this.updateButtons(this.sessionService.currentSession);
	}

	private updateButtons(userState: UserSession | AnonymousSession) {
		if (isUser(userState)) {
			this.header.removeItem(this.loginButton);
			this.header.removeItem(this.registerButton);
			this.header.append(this.logoutButton);
		} else {
			this.header.removeItem(this.logoutButton);
			this.header.append(this.registerButton);
			this.header.append(this.loginButton);
		}
	}
}

const LOGIN = `/${ROOT_USER}/${PATH_LOGIN}`;
const REGISTER = `/${ROOT_USER}/${PATH_REGISTER}`;
