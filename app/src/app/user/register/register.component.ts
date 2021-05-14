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

import {Component, Input, OnInit} from '@angular/core';
import {AuthService} from "@app/service/auth.service";
import {NavigationService} from "@app/service/navigation.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

	@Input()
	goHomeAfterLogin = true;

	model = {
		name: '',
		username: '',
		password: '',
		passwordConfirmation: ''
	};

	usernameUnavailable = false;
	passwordMismatch = false;

	error: string = '';
	loading = false;

	constructor(private readonly authService: AuthService,
	            private readonly navigationService: NavigationService) {
	}

	ngOnInit(): void {
	}

	async submit() {
		if (this.model.password == this.model.passwordConfirmation) {
			this.loading = true;
			try {
				await this.authService.createNew(
					this.model.name,
					this.model.username,
					this.model.password);
				if (this.goHomeAfterLogin) {
					await this.navigationService.home();
				}
			} catch (e) {
				this.error = e.message;
			}
			this.loading = false;
		} else {
			this.passwordMismatch = true;
		}
	}
}
