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
import {UserGroup} from "@app/model/user-group";
import {Shop} from "@app/model/shop";
import {environment} from "@environments/environment";

const SHOP_ROOT = environment.apiUrl + '/shop';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.20
 */
@Injectable({
	providedIn: 'root'
})
export class ShopService {

	private readonly _shopsChanged = new Subject<any>();

	readonly $shopsChanged: Observable<any> = this._shopsChanged.asObservable();

	constructor(private readonly httpClient: HttpClient) {
	}

	async create(name: string, group: UserGroup): Promise<Shop> {
		return this.httpClient.post<UserGroup>(SHOP_ROOT, {
			groupId: group.id,
			name: name
		}).toPromise();
	}

	async getShop(shopId: string): Promise<Shop> {
		return this.httpClient.get<Shop>(`${SHOP_ROOT}/${shopId}`).toPromise();
	}

	async getAll(group: UserGroup): Promise<Shop[]> {
		const params = new HttpParams().append('groupId', group.id);
		const url = SHOP_ROOT + params.toString();
		return await this.httpClient.get<Shop[]>(url).toPromise();
	}
}
