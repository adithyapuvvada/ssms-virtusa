import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable,map,tap } from "rxjs";

@Injectable({providedIn:'root'})
export class ReportService{
    private baseUrl:string = 'http://localhost:8089/ssms/billing/reports';

    constructor(private httpClient:HttpClient){}

    getPaidReports():Observable<Invoice[]>{
        const getPaidReportsUrl = `${this.baseUrl}/paid`;
        return this.httpClient.get<Invoice[]>(getPaidReportsUrl);
    }

    getUnPaidReports():Observable<Invoice[]>{
        const getUnPaidReportsUrl = `${this.baseUrl}/unpaid`;
        return this.httpClient.get<Invoice[]>(getUnPaidReportsUrl);
    }

    getRevenue():Observable<any>{
        const getRevenueUrl = `${this.baseUrl}/revenue`;
        return this.httpClient.get<any>(getRevenueUrl);
    }

    getPaidReportsCount():Observable<number>{
        return this.getPaidReports().pipe(
            map(invoices=>invoices.length)
        );
    }

    getUnPaidReportCount():Observable<number>{
        return this.getUnPaidReports().pipe(
            map(invoices=>invoices.length),
            tap(count=>console.log("invoices length : ",count))
        );
    }
}