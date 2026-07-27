import { Routes } from '@angular/router';
import { LoginComponent } from './core/auth/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { BillingComponent } from './modules/billing-service/billing.component';
import { Layout } from './shared/layout/layout.component';
import { UserMain } from './modules/user-service/user-service.component';
import { Dashboard } from './shared/dashboard/dashboard.component';
import { InventoryComponent } from './modules/billing-service/Inventory/inventory.component';
import { InvoiceComponent } from './modules/billing-service/invoice/invoice.component';
import { ReportComponent } from './modules/billing-service/reports/report.component';
import { ShipmentComponent } from './modules/shipment-service/shipment/shipment.component';
import { Shipment } from './modules/shipment-service/shipment-service.component';
import { WareHouseComponent } from './modules/shipment-service/warehouse/warehouse.component';
import { UsersComponent } from './modules/user-service/users/users.component';
import { ShippersComponent } from './modules/user-service/shippers/shippers.component';
import { PaymentComponent } from './modules/billing-service/payment/payment.component';

export const routes: Routes = [
    {path:'login',component:LoginComponent},
    
    {
        path:'',component:Layout,canActivate:[authGuard],
        children:[
            {path:'dashboard',component:Dashboard},

            {path:'shipment-service',component:Shipment,canActivate:[authGuard],data:{roles:['ROLE_MANAGER', 'ROLE_INVENTORY_MANAGER','ROLE_SUPPLIER','ROLE_ADMIN']}},
            
            {path:'shipment-service/shipments',component:ShipmentComponent,canActivate:[authGuard],data: { roles: ['ROLE_SUPPLIER','ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_INVENTORY_MANAGER']}},

            {path:'shipment-service/warehouse',component:WareHouseComponent,canActivate:[authGuard],data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_INVENTORY_MANAGER'] }},

            {path:'billing',component:BillingComponent,canActivate:[authGuard],data: { roles: ['ROLE_SUPPLIER','ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_ACCOUNTANT'] }},

            {path:'billing/inventory',component:InventoryComponent,canActivate:[authGuard],data: { roles: ['ROLE_SUPPLIER','ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_INVENTORY_MANAGER'] }},

            {path:'billing/invoice',component:InvoiceComponent,canActivate:[authGuard],data: { roles: ['ROLE_SUPPLIER','ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_ACCOUNTANT'] }},

            {path:'billing/reports',component:ReportComponent,canActivate:[authGuard],data: { roles: ['ROLE_SUPPLIER','ROLE_SUPPLIER','ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_ACCOUNTANT'] }},

            {path:'billing/payments',component:PaymentComponent,canActivate:[authGuard],data: { roles: ['ROLE_SUPPLIER','ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_ACCOUNTANT'] }},

            {path:'user-service',component:UserMain,canActivate:[authGuard],data: { roles: ['ROLE_ADMIN'] }},

            {path:'user-service/users',component:UsersComponent,canActivate:[authGuard],data: { roles: ['ROLE_ADMIN'] }},

            {path:'user-service/shippers',component:ShippersComponent,canActivate:[authGuard],data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] }}
        ]
    },
];
