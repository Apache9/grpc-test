package com.github.apache9;

import com.github.apache9.proto.GreeterGrpc;
import com.github.apache9.proto.HelloReply;
import com.github.apache9.proto.HelloRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class HelloWorld extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
        responseObserver.onCompleted();
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Server server = ServerBuilder.forPort(0).addService(new HelloWorld()).build();
        server.start();
        int port = server.getPort();
        System.out.println("gRPC server is listening on port " + port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext(true).build();
        GreeterGrpc.GreeterFutureStub futureStub = GreeterGrpc.newFutureStub(channel);
        System.out.println("Server response: "
                + futureStub.sayHello(HelloRequest.newBuilder().setName("zhangduo").build()).get().getMessage());
        channel.shutdown();
        server.shutdown();
    }
}
