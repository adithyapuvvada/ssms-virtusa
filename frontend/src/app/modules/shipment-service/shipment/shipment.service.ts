import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable,map } from "rxjs";
import { Shipment } from "./shipment.model";

@Injectable({providedIn:'root'})
export class ShipmentService{
    private baseUrl:string = 'http://localhost:8089/ssms/shipment/shipments';

    constructor(private httpClient:HttpClient){}

    addShipment(shipmentData:any):Observable<any>{
        return this.httpClient.post(this.baseUrl,shipmentData);
    }

    getAllShipments():Observable<Shipment[]>{
        return this.httpClient.get<Shipment[]>(this.baseUrl);
    }

    getShipmentsCount():Observable<number>{
        return this.getAllShipments().pipe(map(shipment=>shipment.length));
    }
}