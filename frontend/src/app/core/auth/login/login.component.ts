import { Component, OnInit } from "@angular/core";
import { AuthService } from "../auth/auth.service";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";

@Component({
    selector:'login-component',
    templateUrl: 'login.component.html',
    styleUrl:'login.component.css',
    imports:[CommonModule,ReactiveFormsModule]
})

export class LoginComponent implements OnInit{
    isSubmitting:boolean = false;
    loginForm!:FormGroup;

    constructor(
        private authService:AuthService,
        private router:Router,
        private fb:FormBuilder
    ){}
    
    ngOnInit() {
        this.initForm();
    }

    initForm(){
        this.loginForm = this.fb.group({
            username : ['',[Validators.required]],
            password : ['',[Validators.required]]
        })
    }

    onLogin(){
        
        if(this.loginForm.valid){
            const loginData = this.loginForm.value;
        this.isSubmitting = true;

        this.authService.login(loginData).subscribe({
            next:(res:any) => {
                this.isSubmitting = false;
                const user = JSON.parse(atob(res.token.split('.')[1]));
                alert("login success as "+user.role);
                console.log("login success",res);
                this.authService.saveToken(res.token);
                this.router.navigate(['/dashboard']);
            },
            error:(err)=>{
                alert("Login Failed! please check username and password");
                this.isSubmitting=false;
            }
        });
        }
    }
}