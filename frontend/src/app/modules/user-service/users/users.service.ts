import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable,map,tap } from "rxjs";
import { Users } from "./users.model";

@Injectable({providedIn:'root'})
export class UserService{
    private baseUrl:string = 'http://localhost:8089/ssms/userservice/users';
    constructor(private httpClient:HttpClient){}

    createUser(userData:any):Observable<any>{
        return this.httpClient.post(this.baseUrl,userData);
    }

    getAllUsers():Observable<Users[]>{
        return this.httpClient.get<Users[]>(this.baseUrl);
    }

    getUsersCount():Observable<number>{
        return this.getAllUsers().pipe(map(users=>users.length))
    }
}