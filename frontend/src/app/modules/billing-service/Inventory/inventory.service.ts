import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({providedIn:'root'})
export class InventoryService{
    private baseUrl:string = 'http://localhost:8089/ssms/billing/inventory';

    constructor(private httpClient:HttpClient){}

    getAllInventories():Observable<inventory[]>{
        const getAllInventoriesUrl = `${this.baseUrl}/all`;
        return this.httpClient.get<inventory[]>(getAllInventoriesUrl);
    }
}