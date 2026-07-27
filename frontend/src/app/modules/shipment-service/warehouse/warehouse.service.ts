import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable,map } from "rxjs";
import { Warehouse } from "./warehouse.model";

@Injectable({providedIn:'root'})
export class WarehouseService{
    private baseUrl:string = 'http://localhost:8089/ssms/shipment/warehouses';

    constructor(private httpClient:HttpClient){}

    createWarehouse(warehouseData:any):Observable<any>{
        return this.httpClient.post(this.baseUrl,warehouseData);
    }

    getAllWarehouses():Observable<Warehouse[]>{
        return this.httpClient.get<Warehouse[]>(this.baseUrl);
    }

    getWarehousesCount():Observable<number>{
        return this.getAllWarehouses().pipe(
            map(warehouse => warehouse.length)
        );
    }   
}