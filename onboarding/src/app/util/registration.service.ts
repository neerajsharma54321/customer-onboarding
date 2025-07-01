import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationLeastResponseDto, RegistrationResponseDto } from '../applicant/registrationResponseDto';
import { catchError, Observable, throwError } from 'rxjs';
import { ApiError } from './error.response.dto';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private baseUrl = 'http://localhost:8093/api';

  constructor(private http: HttpClient) { }

  registerApplication(payload: any, file: File): Observable<{ applicationId: string }> {
    const formData = new FormData();

    formData.append('payload', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    formData.append('documents', file);

    return this.http.post<{ applicationId: string }>(`${this.baseUrl}/onboarding/register`, formData).pipe(
      catchError((error: HttpErrorResponse) => {
        return this.handleApiError<ApiError>(error);
      })
    );
  }

  public findApplicantById(appId: string): Observable<RegistrationResponseDto> {
    return this.http.get<RegistrationResponseDto>(`${this.baseUrl}/onboarding/${appId}`).pipe(
      catchError((error: HttpErrorResponse) => {
        return this.handleApiError<ApiError>(error);
      })
    );
  }


  public findApplicationStatusById(appId: string): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/onboarding/status/${appId}`).pipe(
      catchError((error: HttpErrorResponse) => {
        return this.handleApiError<ApiError>(error);
      })
    );
  }

  public updateApplicationStatusById(appId: string, status: string): Observable<RegistrationResponseDto> {
    return this.http.put<RegistrationResponseDto>(`${this.baseUrl}/onboarding/status/${appId}`, { status }).pipe(
      catchError((error: HttpErrorResponse) => {
        return this.handleApiError<ApiError>(error);
      })
    );
  }

  public findApplicationsByStatus(status?: string[]): Observable<ApplicationLeastResponseDto[]> {
    let params = new HttpParams();
    if(status) {
      status.forEach(d => params = params.append('status', d));
    }

    return this.http.get<ApplicationLeastResponseDto[]>(`${this.baseUrl}/onboarding/applications`, {params}).pipe(
      catchError((error: HttpErrorResponse) => {
        return this.handleApiError<ApiError>(error);
      })
    );
  }

  private handleApiError<T>(error: HttpErrorResponse): Observable<never> {
    const typedError = error.error as T;
    console.error('API Error:', typedError);
    return throwError(() => typedError);
  }
}
