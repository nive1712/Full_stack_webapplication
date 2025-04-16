import { NgModule, NO_ERRORS_SCHEMA  } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule ,HTTP_INTERCEPTORS} from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AtmComponent } from './dashboard/atm/atm.component';
import { NetbankingComponent } from './dashboard/netbanking/netbanking.component';
import { DepositComponent } from './atm/deposit/deposit.component';
import { AtmService } from './atm.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms'; 
import { WithdrawComponent } from './atm/withdraw/withdraw.component';
import { NetBankingDepositComponent } from './netbanking/netbanking-deposit/netbanking-deposit.component';
import { TransferComponent } from './atm/transfer/transfer.component';
import { NetBankingTransferComponent } from './netbanking/netbanking-transfer/netbanking-transfer.component';
import { LoanApplicationComponent } from './netbanking/loan-application/loan-application.component';
import { LoanPaymentComponent } from './netbanking/loan-payment/loan-payment.component';
import { BudgetCalculatorComponent } from './netbanking/budget-calculator/budget-calculator.component';
import { CardBlockComponent } from './netbanking/card-block/card-block.component';
import { CardUnblockComponent } from './netbanking/card-unblock/card-unblock.component';
import { AdminService } from './admin/admin.service';
import { AdminApprovalComponent } from './dashboard/admin-approval/admin-approval.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { AdminRejectionComponent } from './dashboard/admin-rejection/admin-rejection.component';
import { AdminComponent } from './dashboard/admin/admin.component';
import { ComponentsComponent } from './components/components.component';
import { AboutUsComponent } from './components/about-us/about-us.component';
import { ContactUsComponent } from './components/contact-us/contact-us.component';
import { FaqComponent } from './components/faq/faq.component';
import { FooterComponent } from './components/footer/footer.component';
import { ViewProfileComponent } from './dashboard/viewprofile/viewprofile.component';
import { UploadPictureComponent } from './dashboard/upload-picture/upload-picture.component';
import { AuthInterceptor } from './auth.interceptor';
import { ForbiddenComponent } from './forbidden/forbidden.component';

import { LogoutComponent } from './logout/logout.component';
import { NgChartsModule } from 'ng2-charts';
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    DashboardComponent,
    AtmComponent,
    NetbankingComponent,
    DepositComponent,
    AtmComponent,
    
    WithdrawComponent,
    TransferComponent,
    NetBankingDepositComponent,
    NetBankingTransferComponent,
    LoanApplicationComponent,
    LoanPaymentComponent,
    BudgetCalculatorComponent,
    CardBlockComponent,
    CardUnblockComponent,
    AdminApprovalComponent,
    AdminLoginComponent,
    AdminRejectionComponent,
    AdminComponent,
    ComponentsComponent,
    AboutUsComponent,
    ContactUsComponent,
    FaqComponent,
    FooterComponent,
    ViewProfileComponent,
    UploadPictureComponent,
    ForbiddenComponent,
   
    LogoutComponent,
   
  ],
  imports: [
    BrowserModule,
    NgChartsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule ,
    CommonModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  schemas: [NO_ERRORS_SCHEMA],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true, 
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
