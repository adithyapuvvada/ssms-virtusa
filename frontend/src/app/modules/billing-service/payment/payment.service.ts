import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable,catchError,map, throwError, timeout } from "rxjs";
import { PaymentModel } from "./payment.model";

@Injectable({providedIn:'root'})
export class PaymentService{
    private baseUrl:string = 'http://localhost:8089/ssms/billing/payment';

    constructor(private httpClient:HttpClient){}

    makePayment(paymentData:any):Observable<any>{
        return this.httpClient.post(`${this.baseUrl}/pay`, paymentData).pipe(
            timeout(5000),
            catchError(err=>{
                if(err.status === 0 || err.name === 'TimeoutError'){
                    return throwError(()=>new Error('SERVICE_OFFLINE'));
                }
                return throwError(()=>err);
            })
        );
    }

    getAllPayments():Observable<PaymentModel[]>{
        return this.httpClient.get<PaymentModel[]>(this.baseUrl);
    }

    getPaymentsCount():Observable<number>{
        return this.getAllPayments().pipe(map(payment=>payment.length));
    }
}