import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AtmComponent } from './dashboard/atm/atm.component';
import { DepositComponent } from './atm/deposit/deposit.component';
import { UploadPictureComponent } from './dashboard/upload-picture/upload-picture.component';
import { WithdrawComponent } from './atm/withdraw/withdraw.component';
import { TransferComponent } from './atm/transfer/transfer.component';
import { NetbankingComponent } from './dashboard/netbanking/netbanking.component';
import { NetBankingDepositComponent } from './netbanking/netbanking-deposit/netbanking-deposit.component';
import { NetBankingTransferComponent } from './netbanking/netbanking-transfer/netbanking-transfer.component';
import { LoanApplicationComponent } from './netbanking/loan-application/loan-application.component';
import { LoanPaymentComponent } from './netbanking/loan-payment/loan-payment.component';
import { BudgetCalculatorComponent } from './netbanking/budget-calculator/budget-calculator.component';
import { CardBlockComponent } from './netbanking/card-block/card-block.component';
import { CardUnblockComponent } from './netbanking/card-unblock/card-unblock.component';
import { AdminApprovalComponent } from './dashboard/admin-approval/admin-approval.component';
import { AdminRejectionComponent } from './dashboard/admin-rejection/admin-rejection.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { AdminComponent } from './dashboard/admin/admin.component';
import { ViewProfileComponent } from './dashboard/viewprofile/viewprofile.component'; 
import { ComponentsComponent } from './components/components.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { LogoutComponent } from './logout/logout.component';

//import { RoleGuard } from './role.guard';
const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'components', component: ComponentsComponent },
  { path: 'admin/login', component: AdminLoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent},
  { path: 'forbidden', component: ForbiddenComponent },
  { path: 'logout', component: LogoutComponent },
  
  
  
  { path: 'atm', component: AtmComponent, children: [
      { path: 'deposit', component: DepositComponent },
      { path: 'withdraw', component: WithdrawComponent },
      { path: 'transfer', component: TransferComponent }
    ]
  },
  { path: 'netbanking', component: NetbankingComponent, children: [
      { path: 'deposit', component: NetBankingDepositComponent },
      { path: 'transfer', component: NetBankingTransferComponent },
      { path: 'loan', component: LoanApplicationComponent },
      { path: 'pay-loan-due', component: LoanPaymentComponent },
      { path: 'budget-calculator', component: BudgetCalculatorComponent },
      { path: 'card-block', component: CardBlockComponent },
      { path: 'card-unblock', component: CardUnblockComponent },
    ]
  },
  { path: 'viewprofile', component: ViewProfileComponent },
  { path: 'upload-picture', component: UploadPictureComponent },
  // Admin landing page
  { path: 'admin-login', component: AdminLoginComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'admin/approve', component: AdminApprovalComponent },
  { path: 'admin/reject', component: AdminRejectionComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // Default route
  //{ path: '', redirectTo: '/login' } // Wildcard route for a 404 page (optional)
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
