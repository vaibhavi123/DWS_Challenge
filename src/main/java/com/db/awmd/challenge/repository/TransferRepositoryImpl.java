package com.db.awmd.challenge.repository;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.TransferFundException;

@Repository
public class TransferRepositoryImpl implements TransferRepository {

	@Autowired
	public AccountsRepository accountsRepository;

	private Lock lock = new ReentrantLock(true);

	// current thread will acquire lock, till all the operations executed.
	public boolean transfer(Transfer transfer) throws TransferFundException {
		try {
			lock.lock();
			transfer.withdrawFromAccount();
			transfer.depositToAccount();
			accountsRepository.update(transfer.getFrom());
			accountsRepository.update(transfer.getTo());
			lock.unlock();
			return true;
		} finally {
			if (((ReentrantLock) lock).isHeldByCurrentThread()) {
				lock.unlock();
			}

		}
	}
}
