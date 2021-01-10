import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {

	authenticated: boolean = false;

	constructor(private http: HttpClient) {
	}

	async authenticate(credentials: Credentials) {
		const headers = new HttpHeaders(credentials ? {
			authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password)
		} : {});

		const response = await this.http.get<UserPrincipal>('user', {headers: headers}).toPromise();
		this.authenticated = !!response?.name;
	}

}

interface UserPrincipal {
	name: string,
}

interface Credentials {
	username: string,
	password: string
}
