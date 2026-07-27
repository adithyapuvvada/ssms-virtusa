import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { InvoiceService } from "./invoice.service";
import { Router } from "@angular/router";
import { CommonModule } from "@angular/common";

@Component({
    selector:'invoice-service',
    templateUrl:'invoice.component.html',
    styleUrl:'invoice.component.css',
    imports:[CommonModule]
})

export class InvoiceComponent implements OnInit{
    invoices!:Observable<Invoice[]>;

    constructor(private invoiceService:InvoiceService,private router:Router){}

    ngOnInit() {
        this.getAllInvoices();
    }

    getAllInvoices(){
        this.invoices = this.invoiceService.getAllInvoices();
    }

    goBack(){
        this.router.navigate(['/billing']);
    }
}