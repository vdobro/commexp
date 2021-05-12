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

import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

import {AvatarModule} from 'primeng/avatar';
import {MenubarModule} from 'primeng/menubar';
import {DropdownModule} from 'primeng/dropdown';
import {SharedModule as PrimeNgSharedModule} from 'primeng/api';
import {AvatarGroupModule} from 'primeng/avatargroup';

import {UserAvatarGroupComponent} from '@app/shared/user-avatar-group/user-avatar-group.component';
import {GroupSwitcherComponent} from '@app/shared/group-switcher/group-switcher.component';
import {HeaderComponent} from '@app/shared/header/header.component';

import {UserAvatarComponent} from './user-avatar/user-avatar.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.28
 */
@NgModule({
	declarations: [
		UserAvatarGroupComponent,
		GroupSwitcherComponent,
		HeaderComponent,
  UserAvatarComponent,
	],
	exports: [
		UserAvatarGroupComponent,
		HeaderComponent,
		UserAvatarComponent,
	],
	imports: [
		PrimeNgSharedModule,
		FormsModule,
		CommonModule,
		AvatarModule,
		DropdownModule,
		MenubarModule,
		AvatarGroupModule,
	]
})
export class SharedModule {
}
