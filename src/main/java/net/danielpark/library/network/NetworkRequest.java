package net.danielpark.library.network;

import android.os.Looper;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 네트워크 관련 요청 <br>
 * 기본적으로 동기 I/O 를 별도로 처리시켜 비동기 효율을 얻기위한 스케쥴러 {@link io.reactivex.schedulers.Schedulers#io()} 에서
 * 조합한 결과를 {@link io.reactivex.android.schedulers.AndroidSchedulers#mainThread()} 에서 받아 UI 에 반영하는 패턴으로 이루어진다. <br>
 * 기타 참고 자료: https://realm.io/kr/news/rxandroid-4/
 * <p>onError 를 생략할 경우, Http error 발생시 RxJava 에서 java.lang.IllegalStateException | NotImplementedOnError 을 던진다..</p>
 * <br><br>
 * Created by Daniel on 2017-01-11.
 */

public class NetworkRequest {

	public static <T> Disposable asyncRequest(Single<T> single, Consumer<T> onSuccess, Consumer<Throwable> onError){
		if (Looper.myLooper() != Looper.getMainLooper())
			throw new RuntimeException("Execute this on Main thread!");

		return single.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.unsubscribeOn(Schedulers.io())
				.subscribe(onSuccess, onError);
	}

	@Deprecated
	public static <T> Disposable performAsyncRequest(Observable<T> observable, Consumer<? super T> onAction, Consumer<Throwable> onError) {
		if (Looper.myLooper() != Looper.getMainLooper())
			throw new RuntimeException("Do execute this on Main thread!");

		// Specify a scheduler (Scheduler.newThread(), Scheduler.immediate(), ...)
		// We choose Scheduler.io() to perform network request in a thread pool
		return observable.subscribeOn(Schedulers.io())
				// Observe result in the main thread to be able to update UI
				.observeOn(AndroidSchedulers.mainThread())
				// Thread pool 에서 unsubscribe 관리
				.unsubscribeOn(Schedulers.io())
				// Set callbacks actions
				.subscribe(onAction, onError);
	}

	@Deprecated
	public static <T> Disposable performAsyncRequest(Flowable<T> observable, Consumer<? super T> onAction, Consumer<Throwable> onError) {
		if (Looper.myLooper() != Looper.getMainLooper())
			throw new RuntimeException("Do execute this on Main thread!");

		// Specify a scheduler (Scheduler.newThread(), Scheduler.immediate(), ...)
		// We choose Scheduler.io() to perform network request in a thread pool
		return observable.subscribeOn(Schedulers.io())
				// Observe result in the main thread to be able to update UI
				.observeOn(AndroidSchedulers.mainThread())
				// Thread pool 에서 unsubscribe 관리
				.unsubscribeOn(Schedulers.io())
				// Set callbacks actions
				.subscribe(onAction, onError);
	}

	@Deprecated
	public static <T> Disposable performAsyncRequest(Observable<T> observable, Consumer<? super T> onAction, Consumer<Throwable> onError, Action onComplete) {
		if (Looper.myLooper() != Looper.getMainLooper())
			throw new RuntimeException("Do execute this on Main thread!");

		// Specify a scheduler (Scheduler.newThread(), Scheduler.immediate(), ...)
		// We choose Scheduler.io() to perform network request in a thread pool
		return observable.subscribeOn(Schedulers.io())
				// Observe result in the main thread to be able to update UI
				.observeOn(AndroidSchedulers.mainThread())
				// Thread pool 에서 unsubscribe 관리
				.unsubscribeOn(Schedulers.io())
				// Set callbacks actions
				.subscribe(onAction, onError, onComplete);
	}

	/**
	 * 캐시를 활용한 Http call <br>
	 * <p>1. API 호출시 먼저 해당 URL 을 key 로 가지고 있는 캐시 존재시 불러오기</p>
	 * <p>2. 서버 API 호출하여, 캐시와 비교 - 캐시와 다를 경우에만 기존의 캐시 업데이트 및 UI 업데이트 시도</p>
	 *
	 * @param localObservable  로컬 캐시 observable
	 * @param remoteObservable 서버 API observable
	 * @param onAction1        로컬 observable doOnNext()
	 * @param onAction2        서버 observable doOnNext()
	 * @param onError          최종 observable onError()
	 * @param <T>
	 * @return
	 */
	@Deprecated
	public static <T> Disposable performCacheRequest(Observable<? extends T> localObservable,
													   Observable<? extends T> remoteObservable,
													   Consumer<? super T> onAction1,
													   Consumer<? super T> onAction2,
													   Consumer<Throwable> onError) {

		if (Looper.myLooper() != Looper.getMainLooper())
			throw new RuntimeException("Do execute this on Main thread!");

		return Observable.concat(
				localObservable
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.doOnNext(onAction1),
				remoteObservable
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.doOnNext(onAction2)
		)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.unsubscribeOn(Schedulers.io())
				.subscribe(
						new Consumer<T>() {
							@Override
							public void accept(T t) throws Exception {
								// Do nothing
							}
						}, onError);

	}

	@Deprecated
	public static <T> Disposable performSyncRequest(Flowable<T> observable, Consumer<? super T> onAction, Consumer<Throwable> onError) {
//		if (Looper.myLooper() == Looper.getMainLooper())
//			throw new RuntimeException("Do not execute this on Main thread!");

		// Specify a scheduler (Scheduler.newThread(), Scheduler.immediate(), ...)
		// We choose Scheduler.immediate() to perform network request in the current thread (Service)
		return observable.subscribeOn(Schedulers.trampoline())
				// Observe result in the current thread (Service)
				.observeOn(Schedulers.trampoline())
				// current thread 에서 unsubscribe 관리
				.unsubscribeOn(Schedulers.trampoline())
				// Set callbacks actions
				.subscribe(onAction, onError);
	}
}
