# transaction-service
the service perfroms amount transfer between accounts. 

It has single end point TransferController, which internally calls TransferService class.
TransferService calls TransferRepository which has business logic of transfering money between accoounts.
transfer() performs locking mechanism to ensure single thread execution and, account domain object contains AtomicReference to ensure atomicity of data/add/subract operations between threads.



//Impvoment Scope:

 *We can consider @RequestScope so that every request have saperate bean, therefore no need to worry aboout data inconsitency.
 *we can add some extra feature/excpetion/docker/spring security for documentation
 *we can add actuator end points so that we cna check health of the api