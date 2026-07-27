import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { ReportService } from "./report.service";
import { Router, RouterLink } from "@angular/router";
import { CommonModule,Location } from "@angular/common";

@Component({
    selector:'report-service',
    templateUrl:'report.component.html',
    styleUrl:'report.component.css',
    imports:[CommonModule]
})

export class ReportComponent implements OnInit{
    view!:string;
    paidReports!:Observable<Invoice[]>;
    unPaidReports!:Observable<Invoice[]>;

    constructor(private reportService:ReportService,private router:Router,private location:Location){}

    ngOnInit() {
        this.getPaidReports();
        this.getUnPaidReports();
    }

    getPaidReports(){
        this.paidReports = this.reportService.getPaidReports();
    }

    getUnPaidReports(){
        this.unPaidReports = this.reportService.getUnPaidReports();
    }

    goBack(){
        if(window.history.length>2)
            this.location.back();
        else    
            this.router.navigate(['/dashboard']);
    }

}