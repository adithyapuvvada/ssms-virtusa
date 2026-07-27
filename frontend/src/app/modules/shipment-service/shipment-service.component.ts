import { Component, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { AuthService } from "../../core/auth/auth/auth.service";
import { CommonModule } from "@angular/common";

@Component({
    selector:'shipment-service',
    templateUrl:'shipment-service.component.html',
    styleUrl:'shipment-service.component.css',
    imports:[RouterLink,CommonModule]
})

export class Shipment implements OnInit{

    currentRole!:string;

    constructor(private authService:AuthService){}

    ngOnInit(): void {
        this.authService.getRole().subscribe(role => {
        if (role) {
            this.currentRole = role;
            console.log("Dashboard identified role:", this.currentRole);
        }
    })
    }

    checkAccess(allowedRoles:string[]):boolean{
        return this.currentRole?allowedRoles.includes(this.currentRole):false;
    }
}