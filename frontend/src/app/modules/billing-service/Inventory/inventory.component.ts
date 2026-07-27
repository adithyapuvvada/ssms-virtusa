import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { InventoryService } from "./inventory.service";
import { CommonModule } from "@angular/common";

@Component({
    selector:'inventory',
    templateUrl:'inventory.component.html',
    styleUrl:'inventory.component.css',
    imports:[CommonModule]
})

export class InventoryComponent implements OnInit{
    inventories!:Observable<inventory[]>;

    constructor(private router:Router,private inventoryService:InventoryService){}

    ngOnInit() {
        this.inventories = this.inventoryService.getAllInventories();
    }

    goBack(){
        this.router.navigate(['/billing']);
    }
}