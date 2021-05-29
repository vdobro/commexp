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
import {Shop} from "@app/model/shop";
import {UserSession} from "@app/model/user-session";
import {SessionService} from "@app/service/state/session.service";
import {GroupSessionService} from "@app/service/state/group.service";

const URL_ROOT = environment.apiUrl + '/purchase';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.21
 */
@Injectable({
	providedIn: 'root'
})
export class PurchaseService {

	private readonly _purchasesChanged = new Subject<UserGroup>();
	private readonly _purchases: Map<UserGroup, Purchase[]> = new Map<UserGroup, Purchase[]>();

	readonly $purchasesChanged: Observable<UserGroup> = this._purchasesChanged.asObservable();

	constructor(private readonly httpClient: HttpClient,
	            private readonly userSession: SessionService,
	            private readonly session: GroupSessionService) {
		this.session.myGroups$.subscribe(async groups => {
			if (groups.length == 0) {
				this._purchases.clear();

			} else {
				for (let group of groups) {
					await this.getAll(group);
				}
			}
		});
		this.$purchasesChanged.subscribe(async group => {
			await this.getAll(group);
		});
	}

	async getAll(group: UserGroup): Promise<Purchase[]> {
		const params = new HttpParams().append('groupId', group.id);
		const url = URL_ROOT + "?" + params.toString();
		const purchases = await this.httpClient.get<Purchase[]>(url).toPromise()
		this._purchases.set(group, purchases);
		return purchases;
	}

	async create(group: UserGroup, shop: Shop, time: Date) {
		const user = this.userSession.current as UserSession;
		await this.httpClient.post<Purchase>(URL_ROOT, {
			groupId: group.id,
			shopId: shop.id,
			doneBy: user.id,
			shoppingTime: time.toISOString().substring(0, 10)
		});

		this._purchasesChanged.next(group);
	}
}
