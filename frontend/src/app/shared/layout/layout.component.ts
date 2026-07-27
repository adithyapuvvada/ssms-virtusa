import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { Sidebar } from "../sidebar/sidebar.component";
import { Navbar } from "../navbar/navbar.component";

@Component({
    selector:'layout',
    templateUrl:'layout.component.html',
    imports:[RouterOutlet,Sidebar,Navbar]
})

export class Layout{

}