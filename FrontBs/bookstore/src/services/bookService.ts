import { Book } from "../domain/Book";
import { BookUpdateRequest } from "../domain/BookUpdateRequest";
import { handleServerException } from "./serviceUtil";

// In Kubernetes the frontend Nginx proxies /v1/ to the backend service.
// When running locally, point directly at the backend.
const BASE_URL = process.env.REACT_APP_API_URL || '';

export interface BookService {
  createUpdateBook(isbn: string, book: Book): Promise<Book>;
  getBook(id: string): Promise<Book | undefined>;
  getBooks(author: number | undefined): Promise<Book[]>;
  partialUpdateBook(isbn: string, book: BookUpdateRequest): Promise<Book>;
  deleteBook(isbn: string): Promise<void>;
}

export class BookServiceImpl implements BookService {
  async createUpdateBook(isbn: string, book: Book): Promise<Book> {
    const response = await fetch(`${BASE_URL}/v1/books/${isbn}`, {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(book),
    });
    handleServerException(response);
    return (await response.json()) as Book;
  }

  async getBook(id: string): Promise<Book | undefined> {
    const response = await fetch(`${BASE_URL}/v1/books/${id}`, {
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });
    handleServerException(response);
    return (await response.json()) as Book;
  }

  async getBooks(author: number | undefined = undefined): Promise<Book[]> {
    let url = `${BASE_URL}/v1/books`;
    if (author !== undefined) {
      url += `?author=${author}`;
    }

    const response = await fetch(url, {
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });

    const contentType = response.headers.get("content-type");
    console.log("📦 Content-Type:", contentType);

    const text = await response.text();
    console.log("📄 Raw body:", text);

    if (contentType?.includes("application/json")) {
      return JSON.parse(text) as Book[];
    } else {
      throw new Error("Response is not JSON");
    }
  }

  async partialUpdateBook(isbn: string, book: BookUpdateRequest): Promise<Book> {
    const response = await fetch(`${BASE_URL}/v1/books/${isbn}`, {
      method: "PATCH",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(book),
    });
    handleServerException(response);
    return (await response.json()) as Book;
  }

  async deleteBook(isbn: string): Promise<void> {
    const response = await fetch(`${BASE_URL}/v1/books/${isbn}`, {
      method: "DELETE",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    handleServerException(response);
  }
}
