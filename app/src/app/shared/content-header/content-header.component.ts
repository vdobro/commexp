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

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.14
 */
@Component({
	selector: 'app-content-header',
	templateUrl: './content-header.component.html',
	styleUrls: ['./content-header.component.scss']
})
export class ContentHeaderComponent implements OnInit {

	@Input()
	title : string = '';

	@Input()
	enableNavigation: boolean = true;

	@Output()
	onReturn = new EventEmitter();

	constructor() {
	}

	ngOnInit(): void {
	}

	returnHandler() {
		this.onReturn.emit();
	}
}