import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { PaymentModel } from "./payment.model";
import { PaymentService } from "./payment.service";
import { CommonModule, Location } from "@angular/common";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ReportService } from "../reports/report.service";

@Component({
    selector:'payments',
    templateUrl:'payment.component.html',
    styleUrl:'payment.component.css',
    imports:[CommonModule,ReactiveFormsModule]
})

export class PaymentComponent implements OnInit{

    view!:string;
    paymentList!:Observable<PaymentModel[]>;

    paymentForm!: FormGroup;
    unPaidInvoices!:Invoice[];
    paymentModes = ['UPI', 'CASH', 'CARD'];

    //STATUS LOGIC
    paymentStatus: 'IDLE' | 'PROCESSING' | 'SUCCESS' | 'ERROR' = 'IDLE';
    errorMessage: string = '';
    lastReference: string = '';

    constructor(
        private paymentService:PaymentService,
        private location:Location,
        private router:Router,
        private fb:FormBuilder,
        private reportService:ReportService,
        private cdr:ChangeDetectorRef
    ){}

    ngOnInit() {
        this.getAllPayments();
        this.initForm();
        this.loadUnpaidInvoices();
    }

    initForm(){
        this.paymentForm = this.fb.group({
            invoiceId:['',Validators.required],
            amountPaid: [{value:'',disabled:true},Validators.required],
            paymentMode:['',Validators.required]
        })
    }

    loadUnpaidInvoices(){
        this.reportService.getUnPaidReports().subscribe({
            next:(data)=>this.unPaidInvoices = data,
            error: (err)=>console.error("Error loading unpaid invoices", err)
        })
    }

    onInvoiceSelect(event:any){
        const selectedId = event.target.value;
        const inv = this.unPaidInvoices.find(i=>i.invoiceId == selectedId);
        if(inv){
            this.paymentForm.patchValue({amountPaid:inv.amount});
        }
    }

    onPay() {
        if (this.paymentForm.valid) {
            this.paymentStatus = 'PROCESSING';
            this.errorMessage = '';
            
            // getRawValue() includes disabled fields (amountPaid)
            const payload = this.paymentForm.getRawValue();

            this.paymentService.makePayment(payload).subscribe({
                next: (res) => {
                    this.paymentStatus = 'SUCCESS';
                    this.lastReference = res.paymentReference;
                    this.cdr.detectChanges();
                    this.getAllPayments(); 
                    this.loadUnpaidInvoices(); // Refresh dropdown list
                },
                error: (err) => {
                    this.paymentStatus = 'ERROR';
                    this.errorMessage = (err.message === 'SERVICE_OFFLINE') 
                        ? "Server is unreachable. Please try again later." 
                        : "Payment failed. Please check your network.";
                }
            });
        }
    }

    getAllPayments(){
        this.paymentList = this.paymentService.getAllPayments();
    }

    goBack(){
        if(window.history.length > 2){
            this.location.back();
        }
        else{
            this.router.navigate(['/billing/payments']);
        }
    }
}