import { CommonModule, Location } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { Users } from "./users.model";
import { UserService } from "./users.service";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ShipperService } from "../shippers/shippers.service";
import { Shippers } from "../shippers/shippers.model";

@Component({
    selector:'users',
    templateUrl:'users.component.html',
    styleUrl:'users.component.css',
    imports:[CommonModule,ReactiveFormsModule]
})

export class UsersComponent implements OnInit{
    view!:string;
    userList!:Observable<Users[]>;
    userForm!:FormGroup;
    isSubmitting!:boolean;
    companiesList!:Observable<Shippers[]>;

    availableRoles:string[] = [
        'ADMIN', 
        'MANAGER', 
        'INVENTORY_MANAGER', 
        'SUPPLIER',
        'ACCOUNTANT'
    ]

    constructor(
        private location:Location,
        private router:Router,
        private userService:UserService,
        private fb:FormBuilder,
        private shipperService:ShipperService
    ){}

    ngOnInit() {
        this.getAllUsers();
        this.initForm();
        this.companiesList = this.shipperService.getAllShippers();
    }

    initForm(){
        this.userForm = this.fb.group({
            username:['',[Validators.required,Validators.minLength(3)]],
            password:['',[Validators.required,Validators.minLength(6)]],
            role:['',[Validators.required]],
            companyId: [null]
        });

        this.userForm.get('role')?.valueChanges.subscribe(role=>{
            const companyControl = this.userForm.get('companyId');
            if(role === 'SUPPLIER'){
                companyControl?.setValidators([Validators.required]);
            }
            else{
                companyControl?.clearValidators();
                companyControl?.setValue(null);
            }
            companyControl?.updateValueAndValidity();
        })
    }

    getAllUsers(){ 
        this.userList = this.userService.getAllUsers();
    }

    onSubmit(){
        if(this.userForm.valid){
            this.isSubmitting = true;
            this.userService.createUser(this.userForm.value).subscribe({
                next:(res)=>{
                    alert("user added successfully...!");
                    this.isSubmitting = false;
                    this.userForm.reset();
                    this.view = 'view-users';
                    this.getAllUsers();
                },

                error:(err) => {
                    alert("error : "+err.message);
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