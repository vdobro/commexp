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

import {User} from "@app/model/user";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.28
 */
export function generateColor(user: User, saturation: number, lightness: number): string {
	let hash = 0;
	let base = user.id.replace('-', '') + user.username;
	for (let i = 0; i < base.length; i++) {
		hash = base.charCodeAt(i) + ((hash << 5) - hash);
	}
	let h = hash % 360;
	let s = Math.min(saturation, 100);
	let l = Math.min(lightness, 100);
	return `hsl(${h}, ${s}%, ${l}%)`;
}
