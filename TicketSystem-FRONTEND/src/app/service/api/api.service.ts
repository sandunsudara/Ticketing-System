import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Response } from '../../models/response/response';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
   baseUrl: String = 'http://localhost:8080/';

  constructor(private http: HttpClient,private _snackBar: MatSnackBar) {}


  get<T>(endPoint: string):Observable<Response<T>> {
    return this.http
      .get<Response<T>>(`${this.baseUrl}${endPoint}`)
      .pipe(catchError(this.handleError));
}


  post<T>(endPoint: string, body: any): Observable<Response<T>> {
    return this.http
      .post<Response<T>>(`${this.baseUrl}${endPoint}`, body)
      .pipe(catchError(this.handleError));
  }

  put<T>(endPoint:string, body: any):Observable<Response<T>>{
    return this.http
      .put<Response<T>>(`${this.baseUrl}${endPoint}`, body)
      .pipe(catchError(this.handleError))
  }

  private handleError(error: any): Observable<never> {
    // Check if it's a client-side or network error
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred
      this.openSnackBar('Client-side error: ' + error.error.message, 'close', 'error');
    } else {
      // The backend returned an unsuccessful response code
      this.openSnackBar(`Backend error: ${error.status} - ${error.message}`, 'close', 'error');
    }
    // Log the error to the console or send it to an external logging service
    console.error('Error occurred:', error);
    // Return a user-friendly observable
    return throwError('Something went wrong. Please try again later.');
  }

  private openSnackBar(message: string, action: string, type: 'error' | 'success') {
    const snackBarConfig = {
      duration: 5000,
      panelClass: type == 'error' ? 'snack-bar-error' : 'snack-bar-success',
    };
    this._snackBar.open(message, action, snackBarConfig);
  }
}
