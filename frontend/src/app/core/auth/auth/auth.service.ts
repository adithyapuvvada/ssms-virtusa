import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";

@Injectable({providedIn:'root'})
export class AuthService{
    private baseUrl = "http://localhost:8089/ssms/userservice/auth";

    private userRole = new BehaviorSubject<string|null>(this.getRoleFromToken());

    constructor(private httpClient:HttpClient){}

    login(data:any):Observable<any>{
        return this.httpClient.post(`${this.baseUrl}/login`,data);
    }

    saveToken(token:string){
        localStorage.setItem("token",token);
        this.userRole.next(this.getRoleFromToken());
    }

    getRoleFromToken():string|null{
        const token = localStorage.getItem('token');
        if(!token)
            return null;

        try{
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.role || null;
        }
        catch(e){
            return null;
        }
    }

    getCompanyIdFromToken():number|null{
        const token = localStorage.getItem('token');
        if(!token)
            return null;

        try{
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.companyId||null;
        }
        catch(e){
            return null;
        }
    }

    getRole():Observable<string|null>{
        return this.userRole.asObservable();
    }

    getCountryFromToken(): string {
    const token = localStorage.getItem('token');
    if (!token) return 'IN'; // System fallback default
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.country || 'IN';
    } catch (e) {
        return 'IN';
    }
}

getCurrencyFromToken(): string {
    const token = localStorage.getItem('token');
    if (!token) return 'INR'; // System fallback default
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.currencyCode || 'INR';
    } catch (e) {
        return 'INR';
    }
}

    hasRole(expectedRoles: string[]): boolean {
        const currentRole = this.getRoleFromToken();
        return currentRole ? expectedRoles.includes(currentRole) : false;
    }

    isLoggedIn():boolean{
        const token = localStorage.getItem('token');
        if(!token)
            return false;

        try{
            const payload = JSON.parse(atob(token.split('.')[1])); //atob -> decoded base 64
            const expiry = payload.exp;
            const now = Math.floor(new Date().getTime()/1000);

            if(now>=expiry){
                this.logout();
                return false;
            }
            return true;
        }
        catch(e){
            return false;
        }
    }

    logout(){
        localStorage.removeItem('token');
        this.userRole.next(null);
    }
}