import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../authentication.service";
import {HttpClient} from "@angular/common/http";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	title = 'Demo';
	greeting = {};

	constructor(private app: AuthenticationService,
	            private http: HttpClient) {
		http.get('resource').subscribe(data => this.greeting = data);
	}

	ngOnInit(): void {
	}


	authenticated() {
		return this.app.authenticated;
	}

}
