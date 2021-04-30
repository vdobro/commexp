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
import {MenuItem} from "primeng/api";
import {SessionService} from "@app/service/state/session.service";
import {AuthService} from "@app/service/auth.service";
import {Session} from "@app/model/user-session";
import {isUser} from "@app/util/SessionUtils";
import {PATH_LOGIN, PATH_REGISTER, ROOT_GROUPS, ROOT_USER} from "@app/util/UrlConfig";

const LOGIN = `/${ROOT_USER}/${PATH_LOGIN}`;
const REGISTER = `/${ROOT_USER}/${PATH_REGISTER}`;
const GROUPS = `/${ROOT_GROUPS}`;

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class HeaderService {

	private readonly loginButton: MenuItem = {
		label: 'Login',
		icon: 'pi pi-unlock',
		routerLink: LOGIN,
	};
	private readonly registerButton: MenuItem = {
		label: 'Register',
		icon: 'pi pi-user-plus',
		routerLink: REGISTER,
	};
	private readonly logoutButton: MenuItem = {
		label: 'Log out',
		icon: 'pi pi-power-off',
		command: async _ => {
			await this.authService.logout();
		}
	};
	private readonly groupsButton: MenuItem = {
		label: 'Groups',
		icon: 'pi pi-users',
		routerLink: GROUPS,
	}

	get items(): MenuItem[] {
		return [
			this.groupsButton,
			this.loginButton,
			this.registerButton,
			this.logoutButton,
		];
	}

	constructor(
		private readonly sessionService: SessionService,
		private readonly authService: AuthService) {
		this.updateButtons({});
		sessionService.session$.subscribe(user => {
			this.updateButtons(user);
		});
	}

	private updateButtons(session: Session) {
		const loggedIn = isUser(session);
		this.enableLoginAndLogout(!loggedIn);
		this.toggleLogout(loggedIn);
		this.toggleAppSections(loggedIn);
	}

	private enableLoginAndLogout(enable: boolean) {
		this.loginButton.visible = enable;
		this.registerButton.visible = enable;
	}

	private toggleLogout(enable: boolean) {
		this.logoutButton.visible = enable;
	}

	private toggleAppSections(enable: boolean) {
		this.groupsButton.visible = enable;
	}

	removeItem(item: MenuItem): void {
		item.visible = false;
	}
}
