# Frequently Asked Questions

### Why does the container take so long to start?

The container fills the database on the first start of the application. This requires some time.

You can start the application stack with `docker compose up` without the `-d` and see the console output. There it
should say something like: `Saving 2040 projects.`. This will only happen on the first start, when there is no available
data, and it may take a minute.

The application is ready when it says: `Started Application`.

### Why does the container is in a crash-loop on initial startup?

Sometimes the __Database Container__ takes longer to start up, because it creates resources like a __Volume__. This
leads to a `communication link failure` and prevents the start of the __Provider Container__.

To solve this, please restart the entire stack. If this does not work: Restart the stack and stop the
__Provider Container__. Then wait until the __Database Container__ is ready and then start the __Provider Container__
again. This is only the fix for the first startup. After this the `docker compose up` should always work.

### Why does the new version does not start anymore?

The new version could contain a __Database Schema Update__ that is not compatible with the old schema. In this case
there are two options. The first and recommended option is to __delete the old volume__ and restart the stack. If there
is already important data stored in the database, please change to configuration to another `spring.datasource.url` and
migrate your data.

The team tries to not do any severe changes to the database, so we can hopefully avoid the migration.

### No answer found to your problem?

Please don't be shy to create a [new Issue](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/issues/new) with
the tag `Question`. We try to improve the application and the documentation to make the usability as easy as possible.
