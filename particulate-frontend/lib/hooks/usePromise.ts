export type PromiseResponse<T> = [ T | null, Error | null ];

export const usePromise = async <T>(promise: Promise<T>): Promise<PromiseResponse<T>> => {
    try {
        return [await promise, null]
    } catch(e) {
        return [null, e];
    }
}