import { CommonModule, Location } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { Shippers } from "./shippers.model";
import { ShipperService } from "./shippers.service";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";

@Component({
    selector:'shippers',
    templateUrl:'shippers.component.html',
    styleUrl:'shippers.component.css',
    imports:[CommonModule,ReactiveFormsModule]
})

export class ShippersComponent implements OnInit{
    view!:string;
    shippersList!:Observable<Shippers[]>;
    shipperForm!: FormGroup;
    isSubmitting!:boolean;

    constructor(
        private shipperService:ShipperService,
        private location:Location,
        private router:Router,
        private fb:FormBuilder
    ){}

    ngOnInit() {
        this.getAllShippers();
        this.initForm();
    }

    initForm(){
        this.shipperForm = this.fb.group({
            companyName : ['',[Validators.required]],
            email : ['',[Validators.required, Validators.email]],
            phone : ['',[Validators.required,Validators.min(1000000000),Validators.max(9999999999)]],
            country: ['', [Validators.required]]
        })
    }

    getAllShippers(){
        this.shippersList = this.shipperService.getAllShippers(); 
    }

    onSubmit() {
        if (this.shipperForm.valid) {
            this.isSubmitting = true;
            
            // 1. Extract raw form values (contains companyName, email, phone, country)
            const formValue = this.shipperForm.value;
            
            // 2. Map currencyCode based on the chosen country automatically
            const derivedCurrency = formValue.country === 'US' ? 'USD' : 'INR';
            
            // 3. Construct the exact payload your Spring Boot entity expects
            const finalPayload = {
                ...formValue,
                currencyCode: derivedCurrency
            };

            // 4. Pass the enhanced payload to the backend service
            this.shipperService.addShipper(finalPayload).subscribe({
                next: (res) => {
                    alert("Shipper Added Successfully with Currency: " + derivedCurrency);
                    this.isSubmitting = false;
                    this.shipperForm.reset();
                    this.view = 'view-shippers';
                    this.getAllShippers();
                },
                error: (err) => {
                    alert("Error : " + err.message);
                    this.isSubmitting = false;
                }
            });
        }
    }

    goBack(){
        if(window.history.length>2){
            this.location.back();
        }
        else{
            this.router.navigate(['/user-service']);
        }
    }
}
