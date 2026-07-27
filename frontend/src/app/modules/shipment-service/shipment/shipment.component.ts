import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { Shipment } from "./shipment.model";
import { ShipmentService } from "./shipment.service";
import { CommonModule, Location } from "@angular/common";
import { Router } from "@angular/router";
import { Shippers } from "../../user-service/shippers/shippers.model";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ShipperService } from "../../user-service/shippers/shippers.service";
import { AuthService } from "../../../core/auth/auth/auth.service";

@Component({
    selector: 'shipments',
    templateUrl: 'shipment.component.html',
    styleUrl: 'shipment.component.css',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule]
})
export class ShipmentComponent implements OnInit {
    view: string = 'view-shipments';
    shipmentList!: Observable<Shipment[]>;
    shipmentForm!: FormGroup;
    isSubmitting: boolean = false;

    filteredCompanies: Shippers[] = [];
    currentRole: string | null = null;
    currentCompanyId: number | null = null;

    constructor(
        private shipmentService: ShipmentService,
        private router: Router,
        private location: Location,
        private fb: FormBuilder,
        private shipperService: ShipperService,
        private authService: AuthService
    ) {}

    ngOnInit() {
        this.currentRole = this.authService.getRoleFromToken();
        this.currentCompanyId = this.authService.getCompanyIdFromToken();
        
        this.initForm();
        this.getAllShipments();
        this.loadCompanies();
    }

    initForm() {
        this.shipmentForm = this.fb.group({
            companyId: ['', [Validators.required]],
            description: ['', [Validators.required]],
            volume: [null, [Validators.required, Validators.min(1)]]
        });
    }

    loadCompanies() {
        this.shipperService.getAllShippers().subscribe({
            next: (data) => {
                if (this.currentRole === 'ROLE_SUPPLIER' && this.currentCompanyId) {
                    this.filteredCompanies = data.filter(c => c.id === Number(this.currentCompanyId));
                    this.shipmentForm.patchValue({ companyId: this.currentCompanyId });
                } else {
                    this.filteredCompanies = data;
                }
            },
            error: (err) => console.error("Error loading shippers", err)
        });
    }

    onAddShipment() {
        if (this.shipmentForm.valid) {
            this.isSubmitting = true;
            
            const selectedId = this.shipmentForm.value.companyId;
            const companyName = this.filteredCompanies.find(c => c.id == selectedId)?.companyName || 'N/A';

            this.shipmentService.addShipment(this.shipmentForm.value).subscribe({
                next: (res) => {
                    alert(`Shipment added successfully under company: ${companyName}`);
                    this.isSubmitting = false;
                    this.shipmentForm.reset();
                    this.view = 'view-shipments';
                    this.getAllShipments();
                },
                error: (err) => {
                    alert("Error: Could not add shipment.");
                    this.isSubmitting = false;
                }
            });
        }
    }

    getAllShipments() {
        this.shipmentList = this.shipmentService.getAllShipments();
    }

    goBack() {
        if (window.history.length > 2) {
            this.location.back();
        } else {
            this.router.navigate(['/shipment-service']);
        }
    }
}