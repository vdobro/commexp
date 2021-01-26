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
import {MenuItem} from "primeng/api";
import {BehaviorSubject} from "rxjs";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class HeaderService {

	private readonly _items = new BehaviorSubject<MenuItem[]>([]);

	readonly $items = this._items.asObservable();

	private get items(): MenuItem[] {
		return this._items.getValue();
	}

	private set items(val: MenuItem[]) {
		this._items.next(val);
	}

	constructor() {

	}

	replaceItems(v: MenuItem[]): void {
		this.items = v;
	}

	addItem(item: MenuItem, at: number): void {
		const current = this.items;
		current.splice(at, 0, item);
		this.items = current;
	}

	append(item: MenuItem): void {
		const current = this.items;
		current.push(item);
		this.items = current;
	}

	removeItem(item: MenuItem): void {
		this.items = this.items.filter(x => x.id !== item.id);
	}

	clearItems(): void {
		this.items = [];
	}
}
