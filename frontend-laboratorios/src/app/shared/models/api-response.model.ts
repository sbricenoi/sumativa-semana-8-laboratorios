export interface ApiResponse<T> {
  traceId: string;
  code: string;
  message: string;
  data: T;
}

