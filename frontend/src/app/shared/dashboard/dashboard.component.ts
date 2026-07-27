import { Component, OnInit } from "@angular/core";
import { Observable,tap } from "rxjs";
import { ReportService } from "../../modules/billing-service/reports/report.service";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { WarehouseService } from "../../modules/shipment-service/warehouse/warehouse.service";
import { ShipmentService } from "../../modules/shipment-service/shipment/shipment.service";
import { UserService } from "../../modules/user-service/users/users.service";
import { ShipperService } from "../../modules/user-service/shippers/shippers.service";
import { AuthService } from "../../core/auth/auth/auth.service";

@Component({
    selector:'dashboard',
    templateUrl:'dashboard.component.html',
    styleUrl:'dashboard.component.css',
    imports:[CommonModule,RouterLink]
})

export class Dashboard implements OnInit{
    revenue!:Observable<any>;
    paidReportCount!:Observable<number>;
    unPaidReportCount!:Observable<number>;
    shipmentsCount!:Observable<number>;
    warehouseCount!:Observable<number>;
    usersCount!:Observable<number>;
    shippersCount!:Observable<number>;
    currentRole!: string;
    currentCurrencyCode: string = 'INR';

    constructor(
        private reportService:ReportService,
        private wareHouseService:WarehouseService,
        private shipmentService:ShipmentService,
        private userService:UserService,
        private shipperService:ShipperService,
        private authService:AuthService
    ){}

    ngOnInit() {
    this.currentCurrencyCode = this.authService.getCurrencyFromToken();
        this.authService.getRole().subscribe(role => {
        if (role) {
            this.currentRole = role;
            console.log("Dashboard identified role:", this.currentRole);
        }
    })
    
        this.getRevenue();
        this.getPaidReportsCount();
        this.getUnPaidReportCount();
        this.getShipmentsCount();
        this.getWarehouseCount();
        this.getUsersCount();
        this.getShipperCount();
    }

    getRevenue(){
        this.revenue = this.reportService.getRevenue();
    }

    getPaidReportsCount(){
        this.paidReportCount = this.reportService.getPaidReportsCount();
    }

    getUnPaidReportCount(){
        this.unPaidReportCount = this.reportService.getUnPaidReportCount();
        console.log("unpaid report count : ",this.unPaidReportCount);
    }

    getWarehouseCount(){
        this.warehouseCount = this.wareHouseService.getWarehousesCount();
    }

    getShipmentsCount(){
        this.shipmentsCount = this.shipmentService.getShipmentsCount();
    }

    getUsersCount(){
        this.usersCount = this.userService.getUsersCount();
    }

    getShipperCount(){
        this.shippersCount = this.shipperService.getShippersCount();
    }

    checkAccess(allowedRoles:string[]):boolean{
        return this.currentRole?allowedRoles.includes(this.currentRole):false;
    }
}