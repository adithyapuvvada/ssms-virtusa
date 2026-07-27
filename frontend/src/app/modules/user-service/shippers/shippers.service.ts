import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable,map } from "rxjs";
import { Shippers } from "./shippers.model";

@Injectable({providedIn:'root'})
export class ShipperService{
    private baseUrl:string = 'http://localhost:8089/ssms/userservice/shippers';

    constructor(private httpClient:HttpClient){}

    addShipper(shipperData:any):Observable<any>{
        return this.httpClient.post(this.baseUrl,shipperData);
    }

    getAllShippers():Observable<Shippers[]>{
        return this.httpClient.get<Shippers[]>(this.baseUrl);
    }

    getShippersCount():Observable<number>{
        return this.getAllShippers().pipe(map(shipper=>shipper.length));
    }
}