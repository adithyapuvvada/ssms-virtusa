import { CommonModule, Location } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { Warehouse } from "./warehouse.model";
import { WarehouseService } from "./warehouse.service";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";

@Component({
    selector:'warehouse',
    templateUrl:'warehouse.component.html',
    styleUrl:'warehouse.component.css',
    imports:[CommonModule,ReactiveFormsModule]
})

export class WareHouseComponent implements OnInit{
    view!:string;
    wareHouseList!:Observable<Warehouse[]>
    warehouseForm!:FormGroup;
    isSubmitting!:boolean;

    constructor(
        private location:Location,
        private router:Router,
        private wareHouseService:WarehouseService,
        private fb:FormBuilder
    ){}

    ngOnInit() {
    }

    
    initForm(){
        this.warehouseForm = this.fb.group({
            name:['',[Validators.required]],
            location:['',[Validators.required]],
            totalCapacity:['',[Validators.required,Validators.min(1000),Validators.pattern("^[0-9]*$")]]
        })
    }

    createWarehouse(){
        if(this.warehouseForm.valid){
            this.isSubmitting = true;
            this.wareHouseService.createWarehouse(this.warehouseForm.value).subscribe({
                next:(res)=>{
                    alert("warehouse added successfully....!");
                    this.isSubmitting = false;
                    this.warehouseForm.reset();
                    this.view = 'view-warehouses';
                    this.getAllWarehouses();
                },

                error:(err)=>{
                    alert("error : "+err.message);
                    this.isSubmitting = false;
                }
            });
        }
    }

    getAllWarehouses(){
        this.wareHouseList = this.wareHouseService.getAllWarehouses();
    }

    goBack(){
        if(window.history.length>2){
            this.location.back();
        }
        else{
            this.router.navigate(['/shipment-service']);
        }
    }
}