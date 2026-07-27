import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({providedIn:'root'})
export class InvoiceService{
    private baseUrl:string = "http://localhost:8089/ssms/billing/invoice";

    constructor(private httpClient:HttpClient){}

    getAllInvoices():Observable<Invoice[]>{
        const getAllInvoicesUrl = `${this.baseUrl}/all`;
        return this.httpClient.get<Invoice[]>(getAllInvoicesUrl);
    }
}