import { Component, OnInit } from "@angular/core";
import { RouterLink, RouterModule } from "@angular/router";
import { AuthService } from "../../core/auth/auth/auth.service";
import { CommonModule } from "@angular/common";

@Component({
    selector:'sidebar',
    templateUrl:'sidebar.component.html',
    styleUrl:'sidebar.component.css',
    imports:[RouterLink,RouterModule,CommonModule]
})

export class Sidebar implements OnInit{
    currentRole!:string|null;

    constructor(private authService:AuthService){}

    ngOnInit() {
        this.authService.getRole().subscribe(role => {
            this.currentRole = role;
        })
    }

    checkAccess(allowedRoles:string[]):boolean{
        return this.currentRole?allowedRoles.includes(this.currentRole):false;
    }
}