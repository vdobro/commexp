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
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {ClipboardModule} from '@angular/cdk/clipboard';

import {PanelModule} from 'primeng/panel';
import {ButtonModule} from 'primeng/button';
import {CardModule} from "primeng/card";
import {RippleModule} from "primeng/ripple";
import {InputTextModule} from "primeng/inputtext";
import {MessageModule} from "primeng/message";
import {SkeletonModule} from 'primeng/skeleton';

import {SharedModule} from "@app/shared/shared.module";

import {GroupsRoutingModule} from './groups-routing.module';
import {GroupListComponent} from './group-list/group-list.component';
import {CreateGroupComponent} from './create-group/create-group.component';
import {AcceptGroupInvitationComponent} from './accept-group-invitation/accept-group-invitation.component';
import {CreateGroupInvitationComponent} from './create-group-invitation/create-group-invitation.component';
import {EditGroupComponent} from './edit-group/edit-group.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@NgModule({
	declarations: [
		GroupListComponent,
		CreateGroupComponent,
		AcceptGroupInvitationComponent,
		CreateGroupInvitationComponent,
		EditGroupComponent
	],
	imports: [
		ButtonModule,
		CommonModule,
		ClipboardModule,
		GroupsRoutingModule,
		PanelModule,
		CardModule,
		SharedModule,
		RippleModule,
		FormsModule,
		InputTextModule,
		MessageModule,
		SkeletonModule,
	]
})
export class GroupsModule {
}
