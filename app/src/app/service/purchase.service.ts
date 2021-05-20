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
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {environment} from "@environments/environment";
import {Purchase} from "@app/model/purchase";
import {UserGroup} from "@app/model/user-group";

const URL_ROOT = environment.apiUrl + '/purchase';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.21
 */
@Injectable({
	providedIn: 'root'
})
export class PurchaseService {

	private readonly _purchasesChanged = new Subject<any>();

	readonly $purchasesChanged: Observable<any> = this._purchasesChanged.asObservable();

	constructor(private readonly httpClient: HttpClient) {
	}

	async getAll(group: UserGroup) : Promise<Purchase[]> {
		const params = new HttpParams().append('groupId', group.id);
		const url = URL_ROOT + "?" + params.toString();
		return this.httpClient.get<Purchase[]>(url).toPromise();
	}
}
