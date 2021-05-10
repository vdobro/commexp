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
import {SessionService} from "@app/service/state/session.service";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {UsernameError} from "@app/util/SessionUtils";
import {NetworkError} from "@app/util/NetworkError";
import {environment} from "@environments/environment";
import {UserSession} from "@app/model/user-session";
import {NavigationService} from "@app/service/navigation.service";

const URL = {
	USERS: environment.apiUrl + "/user",
	LOGOUT: environment.apiUrl + "/logout",
	SESSION: environment.apiUrl + "/session",
};

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.25
 */
@Injectable({
	providedIn: 'root'
})
export class AuthService {

	constructor(private readonly httpClient: HttpClient,
	            private readonly sessionService: SessionService,
	            private readonly navigationService: NavigationService) {
		this.retrieveAndSetUser()
			.then(user => {
				console.log(`Resuming saved user session for ${user.username}.`);
			}).catch(_ => {
				this.logout().then(_ => {
					console.log("No session to resume or previous session expired.");
				});
			});
	}

	async tryLogin(username: string, password: string) {
		await this.logout();
		const headers = new HttpHeaders(
			{
				Authorization: `Basic ${btoa(username + ':' + password)}`
			}
		);
		await this.retrieveAndSetUser(headers);
		await this.navigationService.home();
	}

	async createNew(name: string,
	                username: string,
	                password: string) {
		this.sessionService.load();
		let response: HttpResponse<any>;
		try {
			await this.refreshCsrf();
			response = await this.httpClient.post(
				URL.USERS, {
					username: username,
					name: name,
					password: password,
				},
				{
					observe: "response",
				}
			).toPromise();
		} catch (e) {
			await this.sessionService.reset();
			throw new UsernameError();
		}
		if (response.ok) {
			await this.tryLogin(username, password);
		} else {
			await this.sessionService.reset();
			throw new NetworkError();
		}
	}

	async logout() {
		await this.sessionService.load();
		await this.refreshCsrf();
		await this.httpClient
			.post(URL.LOGOUT, {observe: "response"})
			.toPromise();
		await this.refreshCsrf();
		await this.sessionService.reset();
	}

	private async refreshCsrf() {
		await this.httpClient.get(URL.SESSION, {observe: "response"}).toPromise();
	}

	private async retrieveAndSetUser(headers: HttpHeaders = new HttpHeaders()): Promise<UserSession> {
		this.sessionService.load();
		const response = await this.httpClient.get<UserSession>(
			URL.USERS, {
				observe: "response",
				headers: headers
			}
		).toPromise();

		const user = response.body!!;
		this.sessionService.setUser(user);
		return user;
	}
}
