import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Response } from '../../models/response/response';

@Injectable({
  providedIn: 'root'
})
export class ApiServiceService {
   baseUrl: String = 'http://localhost:8080/';

  constructor(private http: HttpClient) {}


  get<T>(endPoint: string):Observable<Response<T>> {
    return this.http
      .get<Response<T>>(`${this.baseUrl}${endPoint}`)
      .pipe(catchError(this.handleError));
}


  post<T>(endPoint: String, body: any): Observable<Response<T>> {
    return this.http
      .post<Response<T>>(`${this.baseUrl}${endPoint}`, body)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    // Log the error and provide an observable for error handling
    console.error('An error occurred:', error);
    throw error;
  }
}
