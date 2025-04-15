import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BudgetService } from 'src/app/budget.service';
import { ChartOptions, ChartType, ChartData } from 'chart.js';

@Component({
  selector: 'app-budget-calculator',
  templateUrl: './budget-calculator.component.html',
  styleUrls: ['./budget-calculator.component.css']
})
export class BudgetCalculatorComponent implements OnInit {
  budgetForm!: FormGroup;
  pieChartOptions: ChartOptions = {
    responsive: true,
  };
  pieChartLabels: string[] = ['Total Income', 'Total Expenses', 'Debt Repayment'];
  pieChartData: ChartData<'pie'> = {
    labels: this.pieChartLabels,
    datasets: [{
      data: [],
      backgroundColor: ['#36A2EB', '#FF6384', '#FFCE56']
    }]
  };
  pieChartType: ChartType = 'pie';
  pieChartLegend = true;
  pieChartPlugins = [];
  chartMoveLeft = false; 

  constructor(private fb: FormBuilder, private budgetService: BudgetService) {}

  ngOnInit(): void {
    this.budgetForm = this.fb.group({
      totalIncome: ['', [Validators.required, Validators.min(0)]],
      totalExpenses: ['', [Validators.required, Validators.min(0)]],
      debtRepayment: ['', [Validators.required, Validators.min(0)]]
    });

    this.scrollToForm();
  }

  scrollToForm(): void {
    const formSection = document.getElementById('budgetFormSection');
    if (formSection) {
      formSection.scrollIntoView({ behavior: 'smooth' });
    }
  }

  calculateBudget() {
    if (this.budgetForm.valid) {
      const token = localStorage.getItem('jwtToken');
      if (token) {
        const { totalIncome, totalExpenses, debtRepayment } = this.budgetForm.value;

        this.budgetService.calculateBudget(totalIncome, totalExpenses, debtRepayment, token)
          .subscribe((response: any) => {
            console.log('Budget calculation response:', response);
            this.updateChartData(totalIncome, totalExpenses, debtRepayment);
            
            this.chartMoveLeft = true;

            setTimeout(() => {
              this.chartMoveLeft = false;
            }, 3000); 
          }, (error: any) => {
            console.error('Error calculating budget:', error);
          });
      } else {
        console.error('No JWT token found');
      }
    } else {
      console.error('Form is invalid');
    }
  }

  updateChartData(totalIncome: number, totalExpenses: number, debtRepayment: number) {
    this.pieChartData.datasets[0].data = [totalIncome, totalExpenses, debtRepayment];
  }
}
