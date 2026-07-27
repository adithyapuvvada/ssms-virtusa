import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    selector:'navbar',
    templateUrl:'navbar.component.html',
    imports:[]
})

export class Navbar{

    constructor(private router:Router){}

    logout(){
        localStorage.removeItem('token');
        this.router.navigate(['login']);
    }
}