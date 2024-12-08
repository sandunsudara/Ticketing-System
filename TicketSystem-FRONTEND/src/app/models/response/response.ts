export interface Response<T> {
    success : boolean;
    status : string;
    message : string;
    data : T;
}
