import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SharedService, StoreObjKey } from '../util/shared.service';
import { Router } from '@angular/router';
import { AuthService } from '../util/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm!: FormGroup;

  constructor(private fb: FormBuilder, private sharedService: SharedService,
     private router: Router, private auth: AuthService) {}
  
  ngOnInit() {

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Login Data:', this.loginForm.value);
      this.sharedService.updateSharedData(StoreObjKey.SHOW_HEADER_ITEMS, true);
      this.auth.setToken("someToken data saved")
      this.router.navigate(["/processing-officer"])
    }
  }

  ngOnDestroy(): void {
    // this.sharedService.setSharedDataToDefault();
  }
}
